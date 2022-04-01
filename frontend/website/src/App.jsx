/** @format */

import React, {useContext} from 'react';
import styled from 'styled-components';
import './App.css';
import Login from './components/Login';
import Main from './components/Main';
import Sidebar from './components/Sidebar';
import {authContext} from './context/authContext';
function App() {
	const {user} = useContext(authContext);
	if (!user) return <Login />;
	return (
		<Wrapper>
			<Sidebar />
			<Main />
		</Wrapper>
	);
}
const Wrapper = styled.div`
	display: flex;
	color: white;
	max-height: 100vh;
`;
export default App;
