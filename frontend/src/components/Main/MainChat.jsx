/** @format */

import axios from 'axios';
import React, {useContext, useEffect, useRef} from 'react';
import styled from 'styled-components';
import {findAllMessages} from '../../apiUrls';
import {authContext} from '../../context/authContext';
import {context as socketContext} from '../../context/SocketContext';
import background from '../../media/chat-background.jpeg';
const MainChat = () => {
  // Dummy data for demonstration
  const dummyMessages = [
	{ sentBy: 'user1', content: 'Hello!' },
	{ sentBy: 'user2', content: 'Hi, how are you?' },
	{ sentBy: 'user1', content: 'I am good, thanks! And you?' },
	{ sentBy: 'user2', content: 'Doing well, excited for this chat app!' },
  ];
  const [messages, setMessages] = React.useState(dummyMessages);
  const { NewMessage, room } = useContext(socketContext);
//   const { user } = useContext(authContext);
  const user={
	_id:'user1'
  }

  // Commented out API fetching for demo
  // useEffect(() => {
  //   (async () => {
  //     if (!room || !room.roomId) return;
  //     const res = await axios.get(findAllMessages(room?.roomId));
  //     console.log(res.data.data);
  //     setMessages(res.data.data);
  //   })();
  // }, [room?.roomId]);

  useEffect(() => {
	if (!NewMessage) return;
	setMessages(messages.concat(NewMessage));
  }, [NewMessage]);

  return (
	<ChatArea>
	  {messages?.slice().reverse().map((message, index) => (
		message.sentBy === user._id ? (
		  <MyMessage content={message.content} key={index} />
		) : (
		  <FriendMessage content={message.content} key={index} />
		)
	  ))}
	</ChatArea>
  );
};
/**
 * 	sentTo: {
	content: {
	sentBy:{
	creationDate:{
	roomId:{
 */
const ChatArea = styled.div`
	flex: 25;
	padding: 0 3rem;
	background-color: var(--whatsapp-light-dark);
	background-image: url(${background});
	overflow-y: scroll;
	::-webkit-scrollbar {
		width: 10px;
	}
	::-webkit-scrollbar-thumb {
		background-color: var(--whatsapp-light-dark);
	}
	::-webkit-scrollbar-track {
		background-color: var(--whatsapp-dark);
	}
`;

const Message = styled.div`
	display: flex;
	justify-content: ${props => (props.position === 'right' ? 'flex-end' : 'flex-start')};
	width: 100%;
`;
const Color = styled.div`
	background-color: ${props => props.color};
	border-radius: 30px;
	padding: 5px 10px;
	margin: 10px 0;
`;
const MyMessage = ({content}) => (
	<Message position='right'>
		<div>
			<Color color='green'>{content}</Color>
		</div>
	</Message>
);
const FriendMessage = ({content}) => (
	<Message position='left'>
		<div>
			<Color color='grey'>{content}</Color>
		</div>
	</Message>
);

export default MainChat;
