import React from 'react';
import { Button, IconButton } from '@mui/material'
import {Dvr, AddCard, Terminal} from '@mui/icons-material'


export default function About() {

    return (
        <>
            <div>
                <h1 style={{fontSize:'5em', width:'.3fr'}}>Welcome to <br/> KingScript</h1>
                <div class="multiline colored" style={{marginBottom:'80px'}}>
                    KingScript is a Roblox Studio package manager and command line interface. <br/><br/>
                    Using KingScript helps improve development times dramatically!
                </div>
            </div>
            <div style={{backgroundColor:'rgb(30,38,45)', boxShadow:'0px 0px 20px',padding:'30px 30px'}}>

                <h2 style={{fontSize:'2em', padding:'0px 0px'}}>Installing KingScript to your Studio</h2>

                <p>You can purchase the KingScript plugin at the Creator Marketplace here!</p>
                
                <Button color='secondary' variant="outlined" href=''>Get Plugin {'>'}</Button>

                <p>To learn how to setup KingScript and how to use its features checkout the documentation!</p>

                <Button color='secondary' variant="outlined" href='/Documentation'>Documentation {'>'}</Button>

            </div>
            <div style={{padding:'40px 40px'}}>
                
                <h1 style={{fontSize:'3em', width:'.3fr'}}>Features</h1>
                
                <IconButton href='/Documentation' sx={{flexDirection:'column', margin: '30px 30px'}}>
                    <AddCard sx={{color: 'purple', fontSize: '3em'}}/>
                    <p>Modules</p>
                </IconButton>
                
                <IconButton href='/Documentation' sx={{flexDirection:'column', margin: '30px 30px'}}>
                    <Terminal sx={{color: 'purple', fontSize: '3em'}}/>
                    <p>Command Line</p>
                </IconButton>

                <IconButton href='/Documentation' sx={{flexDirection:'column', margin: '30px 30px'}}>
                    <Dvr sx={{color: 'purple', fontSize: '3em'}}/>
                    <p>Systems</p>
                </IconButton>

            </div>
        </>
    )
}

