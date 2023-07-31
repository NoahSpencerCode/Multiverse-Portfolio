import React, { useState } from 'react';
import TextField from '@mui/material/TextField';
import { Container ,Paper,Button} from '@mui/material';
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom";


export default function Edit() {
    const paperStyle={padding:'50px 20px',margin:"20px auto",backgroundColor: "rgb(30,38,45)"}
    

    const location = useLocation()
    const { Id,Title,Body } = location.state

    const[title,setTitle]=useState(Title)
    const[body,setBody]=useState(Body)
    

    let navigate = useNavigate(); 


  const handleClick=(e)=>{
    e.preventDefault()
    const addition={title,body}
    console.log(addition)
    fetch(`http://localhost:8080/addition/${Id}`,{
      method:"PUT",
      headers:{"Content-Type":"application/json"},
      body:JSON.stringify(addition)

  }).then(()=>{
    console.log("Addition updated")
    let path = `/Addition`; 
    navigate(path);
  })
}




  return (

    <Container>
        <Paper elevation={3} style={paperStyle}>
            <h1>Edit Addition</h1>

    <form noValidate autoComplete="off">
    
      <TextField id="outlined-basic" label="Title" variant="outlined" fullWidth style={{margin: '10px 0'}}
      value={title}
      onChange={(e)=>setTitle(e.target.value)}
      
      />
      <TextField id="outlined-basic" label="Body" variant="outlined" fullWidth style={{margin: '10px 0'}} multiline rows={10}
      value={body}
      onChange={(e)=>setBody(e.target.value)}
      />
      <Button variant="contained" color="secondary" onClick={handleClick}>
  Submit
</Button>
    </form>
   
    </Paper>

    </Container>
  );
}