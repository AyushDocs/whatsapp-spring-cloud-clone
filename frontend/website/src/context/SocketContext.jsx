/** @format */

import {createContext, useEffect, useRef, useState} from 'react';
import {io} from 'socket.io-client';

export const context = createContext();

const SocketProvider = ({children}) => {
	const [socket, setSocket] = useState(null);
	const [NewMessage,setNewMessage] = useState({});
	const [room, setRoom] = useState();

	useEffect(() => {
		console.log('socket opened')
		setSocket(io('http://localhost:3001'));
		return () => {
			console.log('socket closed');
			socket.close();
		};
	}, []);
	useEffect(() => {
		if (!socket) return;
		socket.on('new-message', handleNewMessage);
		return () => {
			console.log('socket closed');
			socket.close();
		};
	}, [socket]);
	function handleNewMessage(data){
		setNewMessage( data)
	};
	const joinRoom = ({roomId, joinerId, room:roomObj}) => {
		if(room && room.roomId === roomId) return;
		setRoom({roomId,...roomObj});
		socket.emit('join-room', {roomId, joinerId});
	};
	const sendMessage = data => {
		socket.emit('send-message', {...data, roomId: room.roomId});
	};
	const leaveRoom = email => {
		socket.emit('disconnect', {email});
	};
	return <context.Provider value={{room, leaveRoom, sendMessage, joinRoom, NewMessage,socket}}>{children}</context.Provider>;
};
export default SocketProvider;
