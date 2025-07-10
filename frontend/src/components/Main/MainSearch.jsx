/** @format */

import React, {useContext, useState} from 'react';
import {BsFillEmojiSmileFill, BsMic} from 'react-icons/bs';
import {IoMdAttach} from 'react-icons/io';
import styled from 'styled-components';
import {authContext} from '../../context/authContext';
import {context as socketContext} from '../../context/SocketContext';
const MainSearch = () => {
	const {sendMessage, room} = useContext(socketContext);
	const [Text, setText] = useState('');
	const {user} = useContext(authContext);
	const handleSubmit = e => {
		e.preventDefault();
		console.log('form submitted');
		sendMessage({sentBy: user._id, sentTo: room._id, message: Text});
		setText('');
	};
	return (
		<SearchArea onSubmit={handleSubmit}>
			<BsFillEmojiSmileFill style={{marginInline: '5px', fontSize: 20}} />
			<IoMdAttach style={{marginInline: '5px', fontSize: 20}} />
			<SearchContainer>
				<SearchInput value={Text} onChange={e => setText(e.target.value)} placeholder='Type a message' />
			</SearchContainer>
			<BsMic style={{marginInline: '5px', fontSize: 20}} />
		</SearchArea>
	);
};
const SearchArea = styled.form`
	display: flex;
	align-items: center;
	flex: 1;
	background-color: var(--whatsapp-dark);
	padding: 10px 5px;
`;
const SearchContainer = styled.div`
	display: flex;
	align-items: center;

	border-radius: 10px;
	background-color: var(--whatsapp-light-dark);
	width: 100%;
	padding: 0 5px;
`;
const SearchInput = styled.input`
	background: transparent;
	border: 0;
	outline: 0;
	padding: 5px 10px;
	color: white;
	width: 100%;
	height: 30px;
`;
export default MainSearch;
