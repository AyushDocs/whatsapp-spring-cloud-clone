/** @format */

export const findAllRooms = email => `http://localhost:3001/api/rooms?userId=${email}`;
export const createRoom = () => `http://localhost:3001/api/rooms`;
export const findAllMessages = roomId => `http://localhost:3001/api/rooms/${roomId}`;
export const saveMessage = roomId => `http://localhost:3001/api/rooms/${roomId}/messages`;
export const saveUserToRoom = roomId => `http://localhost:3001/api/rooms/${roomId}`;
export const saveUser = () => `http://localhost:3001/api/v1/users/signup`;
export const findUserByNameOrEmail = text => `http://localhost:8078/api/v1/users?text=${text}`;
export const findUserInfoByUserId = (userId) => `http://localhost:3001/api/users/some-fields?userId=${userId}`;
export const uploadImage = userId => `http://localhost:8078/api/v1/images/${userId}`;