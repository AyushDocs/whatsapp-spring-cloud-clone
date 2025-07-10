/** @format */
import {useContext} from 'react';
import {Img} from 'react-image';
import TimeAgo from 'react-timeago';
import styled from 'styled-components';
import {authContext} from '../../context/authContext';
import {context as socketContext} from '../../context/SocketContext';
const props = {
	imgUrl: 'https://images.unsplash.com/photo-1518806118471-f28b20a1d79d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=800&q=60',
	groupName: 'Group Name',
	lastConversation: 'Last Conversation',
	time: 'a day ago',
};
const getDisplayName = (userNamesArr, user) => {
	return userNamesArr[1] === user.displayName ? userNamesArr[0] : userNamesArr[1];
};
const SidebarItem = props => {
	const {roomId, userNamesArr, creationDate, photoUrl,lastMessage} = props.item;
	const {joinRoom} = useContext(socketContext);
	const {user} = useContext(authContext);
	return (
		<Wrapper onClick={() => joinRoom({roomId, joinerId: user._id, room: {...props.item, displayName: getDisplayName(userNamesArr, user)}})}>
			<ImageContainer>
				<Img height={35} src={photoUrl} alt='' />
			</ImageContainer>
			<Container>
				<Title>{getDisplayName(userNamesArr, user)}</Title>
				<Title>{lastMessage}</Title>
				<Time>
					<TimeAgo date={creationDate} />
				</Time>
			</Container>
		</Wrapper>
	);
};
const Wrapper = styled.div`
	position: relative;
	height: 50px;
	width: 100%;
	display: flex;
	align-items: center;
	color: white;
	padding: 30px 0;
	&:hover {
		background-color: hsl(203, 32%, 15%);
		cursor: pointer;
	}
`;
const ImageContainer = styled.div`
	display: flex;
	justify-content: center;
	align-items: center;
`;
const Container = styled.div`
	display: flex;
	padding: 10px 15px;
	align-items: center;
	width: 100%;
	color: white;
	border-bottom: 1px solid grey;
`;
const Time = styled.div`
	position: absolute;
	right: 0.1em;
	top: 1.2em;
	font-size: 0.8em;
	// @media (max-width: 704px) {
	// 	display: none;
	// }
`;
const Title = styled.div`
	font-size: min(1.2em, 2.5vw);
`;
export default SidebarItem;
