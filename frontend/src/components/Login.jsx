/** @format */

import React, {useContext, useState} from 'react';
import styled from 'styled-components';
import {authContext} from '../context/authContext';

const Login = () => {
	const {handleLoginSubmit} = useContext(authContext);
	const [FormData, setFormData] = useState({email: '', password: ''});
	const handleChange = e => setFormData({...FormData, [e.target.name]: e.target.value});
	return (
		<StyledWrapper>
			<StyledForm
				onSubmit={e => {
					e.preventDefault();
					handleLoginSubmit(e, FormData.email, FormData.password);
				}}>
				<StyledLabel htmlFor='login-email'>Email</StyledLabel>
				<StyledInput autoComplete='username' onChange={handleChange} name='email' value={FormData.email} id='login-email' />
				<StyledLabel htmlFor='login-password'>Password</StyledLabel>
				<StyledInput autoComplete='current-password' onChange={handleChange} name='password' value={FormData.password} type='password' id='login-password' />
				<StyledButton type='submit'>Login</StyledButton>
			</StyledForm>
		</StyledWrapper>
	);
};
const StyledWrapper = styled.div`
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	width: 100vw;
	background-color: var(--whatsapp-dark);
`;
const StyledForm = styled.form`
	background-color: rgb(2 41 177);
	display: flex;
	flex-direction: column;
	padding: 10px 15px;
`;
const StyledInput = styled.input`
	margin-block-end: 10px;
	outline: none;
`;
const StyledLabel = styled.label`
	margin-block: 5px;
	outline: none;
`;
const StyledButton = styled.button``;
export default Login;
