/** @format */

import axios from 'axios';
import {createContext, useState} from 'react';
export const authContext = createContext();

const AuthProvider = ({children}) => {
	const [user, setUser] = useState();
	const handleLoginSubmit = async (e, email, password) => {
		e.preventDefault();
		// const res = await axios.post('http://localhost:8078/api/v1/users/login', {email, password}, {withCredentials: true});
		// if (res.status !== 200) return alert('Invalid Credentials');
		setUser({email});
	};
	const handleSignupSubmit = async(formData) => {
		const userId=38
		setUser({...formData,_id:userId})
		// let bodyContent = new FormData();
    	// bodyContent.append('image', file,file.name);
    	// const res=await axios.post(uploadImage(userId) ,bodyContent)
	};
	return <authContext.Provider value={{user, handleLoginSubmit,handleSignupSubmit}}>{children}</authContext.Provider>;
};
export default AuthProvider;
