/** @format */

import React from 'react';
import AllOtherProvider from './allOthers';
import AuthProvider from './authContext';
import SocketProvider from './SocketContext';

const index = ({children}) => {
	return (
		<SocketProvider>
			<AuthProvider>
				<AllOtherProvider>{children}</AllOtherProvider>
			</AuthProvider>
		</SocketProvider>
	);
};

export default index;
