/** @format */

import React, {useContext} from 'react';
import {GoogleLogin} from 'react-google-login';
import styled from 'styled-components';
import {authContext} from '../context/authContext';

const Login = () => {
	const {handleLoginSuccess} = useContext(authContext);
	const clientId = import.meta.env.VITE_GOOGLE_CLIENT_ID;
	console.log(clientId);
	return (
		<Wrapper>
			<Container>
				<GoogleLogin autoLoad clientId={clientId} buttonText='Login with Google' onSuccess={handleLoginSuccess} cookiePolicy={'single_host_origin'} />
			</Container>
		</Wrapper>
	);
};
const Wrapper = styled.div`
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	width: 100vw;
	background-color: var(--whatsapp-dark);
`;
const Container = styled.div`
	background-color: var(--whatsapp-light-dark);
`;
export default Login;
