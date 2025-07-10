/** @format */

import React, { useState, useContext } from 'react';
import styled from 'styled-components';
import { authContext } from '../context/authContext';
import axios from 'axios';
import { uploadImage } from '../apiUrls';

const Signup = () => {
	const {handleSignupSubmit}=useContext(authContext)
	const [formData, setFormData] = useState({
		username: '',
		email: '',
		password: '',
		image: ''
	});

	const handleChange = (e) => {
		const { name, value, files } = e.target;
		if (name === 'image' && files) {
			setFormData({ ...formData, image: files[0] });
		} else {
			setFormData({ ...formData, [name]: value });
		}
	};

	return (
		<Wrapper>
			<Card>
				<Title>Create Account 📝</Title>
				<Form onSubmit={e=>{
					e.preventDefault()
					handleSignupSubmit(formData)
					}}>
					<Label htmlFor="signup-username">Username</Label>
					<Input
						id="signup-username"
						type="text"
						name="username"
						value={formData.username}
						onChange={handleChange}
						placeholder="Your name"
						required
					/>

					<Label htmlFor="signup-email">Email</Label>
					<Input
						id="signup-email"
						type="email"
						name="email"
						value={formData.email}
						onChange={handleChange}
						placeholder="you@example.com"
						autoComplete="username"
						required
					/>

					<Label htmlFor="signup-password">Password</Label>
					<Input
						id="signup-password"
						type="password"
						name="password"
						value={formData.password}
						onChange={handleChange}
						placeholder="••••••••"
						autoComplete="new-password"
						required
					/>

					<Label htmlFor="signup-image">Profile Image</Label>
					<Input
						id="signup-image"
						type="file"
						name="image"
						accept="image/*"
						onChange={handleChange}
					/>

					<Button type="submit">Sign Up</Button>
				</Form>
			</Card>
		</Wrapper>
	);
};

export default Signup;

const Wrapper = styled.div`
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	background-color: var(--whatsapp-dark, #0b141a);
`;

const Card = styled.div`
	background: #ffffff;
	padding: 40px 30px;
	border-radius: 12px;
	box-shadow: 0 0 12px rgba(0, 0, 0, 0.15);
	width: 100%;
	max-width: 400px;
`;

const Title = styled.h2`
	text-align: center;
	margin-bottom: 24px;
	color: #222;
	font-weight: 600;
`;

const Form = styled.form`
	display: flex;
	flex-direction: column;
`;

const Label = styled.label`
	margin-bottom: 8px;
	color: #444;
	font-weight: 500;
`;

const Input = styled.input`
	padding: 10px 12px;
	border: 1px solid #ccc;
	border-radius: 6px;
	margin-bottom: 20px;
	font-size: 14px;
	transition: border 0.2s ease;

	&:focus {
		outline: none;
		border-color: #0256e5;
	}
`;

const Button = styled.button`
	background-color: #0256e5;
	color: white;
	border: none;
	border-radius: 6px;
	padding: 12px;
	font-weight: bold;
	font-size: 15px;
	cursor: pointer;
	transition: background-color 0.3s ease;

	&:hover {
		background-color: #023db8;
	}
`;
