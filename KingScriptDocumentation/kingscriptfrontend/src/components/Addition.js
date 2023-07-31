import React, { useEffect, useState } from 'react';
import TextField from '@mui/material/TextField';
import { Container ,Paper,Button} from '@mui/material';
import Card from './Card'


export default function Addition() {
  const paperStyle={padding:'50px 20px',margin:"20px auto",backgroundColor: "rgb(30,38,45)"}
  const[title,setTitle]=useState('')
  const[body,setBody]=useState('')
  const[additions,setAdditions]=useState([])

  const handleClick=(e)=>{

    e.preventDefault()
    const addition={title,body}
    console.log(addition)

    fetch("http://localhost:8080/addition/add",{

      method:"POST",
      headers:{"Content-Type":"application/json"},
      body:JSON.stringify(addition)

    }).then(()=>{

      console.log("New Addition added")
      window.location.reload(false);

    })
  }   

  useEffect(()=>{
    fetch("http://localhost:8080/addition/getAll")
    .then(res=>res.json())
    .then((result)=>{
      setAdditions(result);
    }
  )
  },[])


  return (

    <Container>
        <Paper elevation={3} style={paperStyle}>
            <h1>New Addition</h1>

          <form noValidate autoComplete="off">
    
            <TextField id="outlined-basic" label="Title" variant="outlined" fullWidth style={{margin: '10px 0'}}
              value={title}
              onChange={(e)=>setTitle(e.target.value)}
            />
            <TextField id="outlined-basic" label="Body" variant="outlined" fullWidth style={{margin: '10px 0'}} multiline rows={10}
              value={body}
              onChange={(e)=>setBody(e.target.value)}
            />
            <Button variant="contained" color="secondary" onClick={handleClick}>Submit</Button>
          </form>
   
        </Paper>
        <h1>Documentation</h1>

        {additions.map(addition=>(
          <Card id={addition.id} title={addition.title} body={addition.body}/>
        ))}
    </Container>
  );
}
