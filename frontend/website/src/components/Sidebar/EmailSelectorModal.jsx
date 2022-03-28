/** @format */

import axios from 'axios';
import React, {useContext} from 'react';
import {Img} from 'react-image';
import Modal from 'react-modal';
import styled from 'styled-components';
import {createRoom, findUserByNameOrEmail} from '../../apiUrls';
import {context} from '../../context/allOthers';
import {authContext} from '../../context/authContext';
const url = 'https://images.unsplash.com/photo-1518806118471-f28b20a1d79d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=800&q=60';
const customStyles = {
	content: {
		top: '50%',
		left: '50%',
		right: 'auto',
		bottom: 'auto',
		marginRight: '-50%',
		transform: 'translate(-50%, -50%)',
		padding: 0,
	},
};
Modal.setAppElement('#root');
const EmailSelectorModal = () => {
	const {toggleModal, openModal} = useContext(context);
	const [search, setSearch] = React.useState('');
	const [searchResults, setSearchResults] = React.useState([]);
	const handleSubmit = async e => {
		e.preventDefault();
		const res = await axios.get(findUserByNameOrEmail(search, search));
		console.log(search);
		const data = res.data.data;
		setSearchResults(data);
	};

	return (
		<div>
			<Modal isOpen={openModal} onRequestClose={toggleModal} style={customStyles}>
				<Container>
					<SearchArea onSubmit={handleSubmit}>
						<SearchContainer>
							<SearchLabel htmlFor='search-person-input' children='Enter email of person you want to find' />
							<SearchInput value={search} onChange={e => setSearch(e.target.value)} id='search-person-input' />
						</SearchContainer>
						<button type='submit'>Submit</button>
					</SearchArea>
					<EmailList>
						{searchResults.sort().map((item, index) => (
							<EmailItem key={index} {...item} />
						))}
					</EmailList>
				</Container>
			</Modal>
		</div>
	);
};
const Container = styled.div`
	width: 300px;
	height: 300px;
	color: white;
	overflow: hidden;
`;
const SearchArea = styled.form`
	display: flex;
	align-items: center;
	flex: 1;
	background-color: var(--whatsapp-dark);
	padding: 10px 5px;
`;
const SearchLabel = styled.label`
	display: flex;
	align-items: center;
	flex: 1;
	background-color: var(--whatsapp-dark);
	padding: 10px 5px;
`;
const SearchContainer = styled.div`
	// display: flex;
	// align-items: center;

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
const EmailList = styled.div`
	overflow-y: auto;
	height: 100%;

	background-color: var(--whatsapp-dark);
	::-webkit-scrollbar {
		width: 10px;
	}
	::-webkit-scrollbar-thumb {
		background-color: grey;
	}
	::-webkit-scrollbar-track {
		background-color: var(--whatsapp-dark);
	}
`;
const EmailSearchItem = styled.div`
	margin: 10px 15px;
	display: flex;
	// align-items: center;
	cursor: pointer;
	:hover {
		background-color: var(--whatsapp-light-dark);
	}
`;
const EmailSearchText = styled.div`
	border-bottom: 1px solid grey;
	padding-block: 20px;
	width: 100%;
`;
const EmailItem = ({displayName, photoUrl}) => {
	const {toggleModal} = useContext(context);
	const {user} = useContext(authContext);
	const handleClick = async () => {
		await axios.post(createRoom(), {userDisplayName: user.displayName, friendDisplayName: displayName});
		toggleModal();
	};
	return (
		<EmailSearchItem onClick={handleClick}>
			<Img height={40} style={{margin: 'auto 5px'}} alt="search person's photo" src={photoUrl} />
			<EmailSearchText>{displayName}</EmailSearchText>
		</EmailSearchItem>
	);
};
export default EmailSelectorModal;
