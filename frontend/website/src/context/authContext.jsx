/** @format */

import axios from 'axios';
import {createContext, useState} from 'react';
import {saveUser} from '../apiUrls';

export const authContext = createContext();
const getUser = () => {
	const profileObj = JSON.parse(localStorage.getItem('user'));
	if (!profileObj) return undefined;
	if (profileObj.timeStamp > Date.now() + 1000 * 60 * 60 * 24) {
		localStorage.removeItem('user');
		return undefined;
	}
	return profileObj;
};
const AuthProvider = ({children}) => {
	const [user, setUser] = useState(getUser());
	const handleLoginSuccess = async googleData => {
		const {email, imageUrl, name} = googleData.profileObj;
		console.log(googleData);
		const res = await axios.post(saveUser(), {email, photoUrl: imageUrl, displayName: name});
		const _user = res.data.data;
		setUser(_user);
		localStorage.setItem('user', JSON.stringify({..._user, timeStamp: Date.now()}));
		setTimeout(() => localStorage.removeItem('user'), 1000 * 60 * 60 * 24);
	};
	return <authContext.Provider value={{user, handleLoginSuccess, setUser: process.env.NODE_ENV !== 'production' && setUser}}>{children}</authContext.Provider>;
};
export default AuthProvider;
