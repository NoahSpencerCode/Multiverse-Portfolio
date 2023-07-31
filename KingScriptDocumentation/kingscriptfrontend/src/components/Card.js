import React from "react";
import { Paper, Button } from '@mui/material';
import { Link } from 'react-router-dom'



export default function Card (props) {

    const handleClick=(e)=>{

        if(window.confirm('Are you sure you want to delete?')){

            e.preventDefault()

            fetch(`http://localhost:8080/addition/${props.id}`,{

                method:"DELETE",
                headers:{"Content-Type":"application/json"},

            }).then(()=>{

                console.log("Addition Deleted")
                window.location.reload(false);

            })
        }
    }

    return (
        <Paper elevation={6} style={{margin:"10px",padding:"15px", textAlign:"left",backgroundColor: "rgb(30,38,45)", display:'flex', justifyContent: 'space-between'}} key={props.id}>
            
            <div>

                <h2>{props.title}</h2>
                <div class="multiline colored">
                    {props.body}
                </div>

            </div>

            <div>

                <Link style={{textDecoration:'none'}} to='/Edit' state={{ Id: props.id, Title: props.title, Body: props.body}}>
                    <Button sx={{height:'40px', marginRight:'10px'}} color='secondary' variant="contained" >Edit</Button>
                </Link>
                <Button sx={{height:'40px', backgroundColor:'red'}} color='secondary' variant="contained" onClick={handleClick}>Delete</Button>
            
            </div>

        </Paper>
    )
}