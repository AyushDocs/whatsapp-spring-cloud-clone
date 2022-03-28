/** @format */

import axios from 'axios';
import React, {useContext, useEffect} from 'react';
import styled from 'styled-components';
import './App.css';
import Login from './components/Login';
import Main from './components/Main';
import Sidebar from './components/Sidebar';
import Provider from './context';
import {authContext} from './context/authContext';
function App() {
	return (
		<Provider>
			<AppRender />
		</Provider>
	);
}
const AppRender = () => {
	const {user, setUser} = useContext(authContext);
	useEffect(() => {
		if (process.env.NODE_ENV !== 'development') return;
		// (async () => {
		// 	const res = await axios.get('http://localhost:3001/api/user/get');
		// 	const _user=res.data;
		// 	localStorage.setItem('user', JSON.stringify({..._user, timeStamp: Date.now()}));
		// 	setUser(_user);
		// })();
	}, []);

	if (!user && process.env.NODE_ENV === 'production') return <Login />;
	return (
		<Wrapper>
			<Sidebar />
			<Main />
		</Wrapper>
	);
};
const Wrapper = styled.div`
	display: flex;
	color: white;
	max-height: 100vh;
`;
export default App;
