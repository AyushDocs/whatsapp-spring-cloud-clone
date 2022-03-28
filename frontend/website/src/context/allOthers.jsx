/** @format */

import {createContext, useState} from 'react';

export const context = createContext();

const AllOtherProvider = ({children}) => {
	const [openModal, setOpenModal] = useState(false)
	const toggleModal = ()=>setOpenModal(o=>!o)
	return <context.Provider value={{openModal, toggleModal}}>{children}</context.Provider>;
};
export default AllOtherProvider;