/** @format */

import React from 'react';
import styled from 'styled-components';
import MainChat from './MainChat';
import MainHeader from './MainHeader';
import MainSearch from './MainSearch';

const Main = () => {
	return (
		<Wrapper>
      <MainHeader/>
      <MainChat/>
			<MainSearch/>
		</Wrapper>
	);
};
const Wrapper = styled.div`
	flex: 0.7;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
`;
export default Main;
