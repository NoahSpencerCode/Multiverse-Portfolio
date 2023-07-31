import React, { useEffect, useState } from 'react';
import { Container ,Paper} from '@mui/material';
import ReactMarkdown from 'react-markdown'
import CodeBlock from './CodeBlock';

export default function Documentation() {
    const[additions,setAdditions]=useState([])


useEffect(()=>{
  fetch("http://localhost:8080/addition/getAll")
  .then(res=>res.json())
  .then((result)=>{
    setAdditions(result);
  })
},[])
  return (
    <Container>

    <h1>Documentation</h1>

    {additions.map(addition=>(
        <Paper elevation={6} style={{margin:"10px",padding:"15px", textAlign:"left",backgroundColor: "rgb(30,38,45)"}} key={addition.id}>
         
         <h2>{addition.title}</h2>
         <div class="multiline colored">
          <ReactMarkdown components={CodeBlock}>{addition.body}</ReactMarkdown>
            
        </div>

        </Paper>
      ))}



    </Container>
  );
}