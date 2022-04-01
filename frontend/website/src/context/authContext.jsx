/** @format */

import axios from 'axios';
import {createContext, useState} from 'react';
export const authContext = createContext();

const AuthProvider = ({children}) => {
	const [user, setUser] = useState();
	const [jwt, setJwt] = useState()
	const handleLoginSubmit=async(e,email,password)=>{
		e.preventDefault();
		const res=await axios.post('http://localhost:8078/api/v1/users/login',{email,password})
		if(!res.status===200) return alert('Invalid Credentials')
		setJwt(res.headers['set-cookie']['token'])
		setUser({email})
	}
	return <authContext.Provider value={{user,handleLoginSubmit,jwt}}>{children}</authContext.Provider>;
};
export default AuthProvider;
