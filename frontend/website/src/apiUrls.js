/** @format */

export const findAllRooms = email => `http://localhost:3001/api/rooms?userId=${email}`;
export const createRoom = () => `http://localhost:3001/api/rooms`;
export const findAllMessages = roomId => `http://localhost:3001/api/rooms/${roomId}`;
export const saveMessage = roomId => `http://localhost:3001/api/rooms/${roomId}/messages`;
export const saveUserToRoom = roomId => `http://localhost:3001/api/rooms/${roomId}`;
export const saveUser = () => `http://localhost:3001/api/users/`;
export const findUserByNameOrEmail = (email, displayName) => `http://localhost:3001/api/users/?email=${email}&displayName=${displayName}`;
export const findUserInfoByUserId = (userId) => `http://localhost:3001/api/users/some-fields?userId=${userId}`;
