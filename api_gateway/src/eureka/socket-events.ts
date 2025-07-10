
import { Server, Socket } from "socket.io";
import axios from "axios";
import client from "./client";
import logger from '../utils/logger';

const socketUserMap = new Map<string, Socket>();

export function setupSocketEvents(io: Server) {
    io.on("connection", (socket: Socket) => {
        const userId = socket.handshake.query.userId as string;

        if (!userId) {
            socket.disconnect(true);
            return;
        }

        socketUserMap.set(userId, socket);
        logger.info(`User ${userId} connected.`);

        socket.on("send-message", async (payload) => {
            const { toUserId, message } = payload;

            // Send to message service via REST
            const instances = client.getInstancesByAppId("MESSAGE-SERVICE");
            if (instances.length > 0) {
                const msgService = instances[0];
                let port: number | undefined;
                if (typeof msgService.port === "number") {
                    port = msgService.port;
                } else if (msgService.port && typeof msgService.port === "object") {
                    port = (msgService.port as any).$ ?? (msgService.port as any).port ?? undefined;
                }
                if (!port) {
                    throw Error('port not set for service');
                }
                const url = `http://${msgService.ipAddr}:${port}/api/v1/messages`;
                try {
                    await axios.post(url, {
                        fromUserId: userId,
                        toUserId,
                        message,
                    });
                } catch (err) {
                    logger.error("Failed to forward message:", err);
                }
            }

            // Relay to recipient if online
            const recipientSocket = socketUserMap.get(toUserId);
            if (recipientSocket) {
                recipientSocket.emit("receive-message", {
                    fromUserId: userId,
                    message,
                });
            }
        });

        socket.on("disconnect", () => {
            logger.info(`User ${userId} disconnected.`);
            socketUserMap.delete(userId);
        });
    });
}
