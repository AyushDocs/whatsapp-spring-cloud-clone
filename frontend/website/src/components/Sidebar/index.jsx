/** @format */

import axios from 'axios';
import React, {useContext, useEffect, useState} from 'react';
import {AiOutlinePlus, AiOutlineSearch} from 'react-icons/all';
import {Img} from 'react-image';
import styled from 'styled-components';
import {findAllRooms} from '../../apiUrls';
import {context} from '../../context/allOthers';
import {authContext} from '../../context/authContext';
import EmailSelectorModal from './EmailSelectorModal';
import SidebarItem from './SidebarItem';

const Sidebar = () => {
	const [Data, setData] = useState([]);
	const {user} = useContext(authContext);
	const {toggleModal} = useContext(context);
	useEffect(() => {
		
		(async () => {
			const res = await axios.get(findAllRooms(user._id));
			console.log(res.data);
			setData(res.data.data);
		})();
	}, []);

	return (
		<Wrapper>
			<Header>
				<HeaderLeft>
					<EmailSelectorModal />

					<Img height={30} style={{margin: 'auto 10px'}} src={user.photoUrl} />
					<HeaderTitle>{user.displayName}</HeaderTitle>
				</HeaderLeft>
				<div style={{cursor: 'pointer'}} onClick={e => toggleModal()}>
					<AiOutlinePlus />
				</div>
			</Header>
			<SearchArea>
				<SearchContainer>
					<AiOutlineSearch color='white' />
					<SearchInput placeholder='Search or input new text' />
				</SearchContainer>
			</SearchArea>
			<ChatArea>
				{Data.map((item, index) => (
					<SidebarItem item={item} key={index} />
				))}
			</ChatArea>
		</Wrapper>
	);
};
const Header = styled.div`
	background-color: var(--whatsapp-light-dark);
	flex: 2;
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 0.5em;
	color: grey;
`;

const HeaderTitle = styled.div`
	font-size: 1.2em;
`;
const HeaderLeft = styled.div`
	display: flex;
	align-items: center;
`;
const Wrapper = styled.div`
	display: flex;
	flex-direction: column;
	flex: 0.3;
`;
//search area,search container,search input
const SearchArea = styled.div`
	display: flex;
	justify-content: center;
	align-items: center;
	flex: 2;
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
`;
const ChatArea = styled.div`
	background-color: var(--whatsapp-dark);
	height: 100vh;
	width: 100%;
	overflow-y: scroll;
	::-webkit-scrollbar {
		width: 10px;
	}
	::-webkit-scrollbar-thumb {
		background: grey;
	}
`;

export default Sidebar;
