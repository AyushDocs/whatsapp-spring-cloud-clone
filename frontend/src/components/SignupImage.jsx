import React, { useRef, useState } from 'react'
import axios from 'axios'
import {uploadImage} from '../apiUrls' 
const SignupImage = () => {
  const fileRef = useRef( undefined )
  //TODO: replace userId
  const userId=56;
  const handleSubmit =async e => {
    e.preventDefault();
    if(fileRef.current==undefined)return;
    const file=fileRef.current.files[0];
    console.log(file)
    let bodyContent = new FormData();
    bodyContent.append('image', file,file.name);
    const res=await axios.post(uploadImage(userId) ,bodyContent)
  }
  return (
    <div>
      <form onSubmit={handleSubmit}>
        <input ref={fileRef} type='file' name='' id='' />
        <input type='submit' value='Submit' />
      </form>
    </div>
  );
}

export default SignupImage