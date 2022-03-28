/** @format */

import React, {useContext} from 'react';
import {AiOutlineSearch} from 'react-icons/ai';
import {BsThreeDots} from 'react-icons/bs';
import styled from 'styled-components';
import {context as SocketContext} from '../../context/SocketContext';
import {Img} from 'react-image'
const MainHeader = () => {
	const {room} = useContext(SocketContext);
	return (
		<Wrapper>
			<div style={{display: 'flex'}}>
				<Img height={40} src={room && room.photoUrl} />
				<div>
					<Title>{room?.displayName||'Title'}</Title>
					<Description>Description</Description>
				</div>
			</div>
			<div>
				<AiOutlineSearch />
				<BsThreeDots />
			</div>
		</Wrapper>
	);
};
const Wrapper = styled.div`
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0.5em 0;
	background-color: var(--whatsapp-dark);
	color: white;
`;
const Title = styled.div`
	font-size: 1.2em;
`;
const Description = styled.div`
	font-size: 0.8em;
	color: grey;
`;
export default MainHeader;
