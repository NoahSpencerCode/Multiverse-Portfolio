import React from "react"
import { Link } from "react-router-dom"


export default function Footer() {


    return (
        <>
            <div style={{padding: '30px 30px', backgroundColor: 'black', color:'white'}}>
            <Link style={{color:'purple'}} to="/Addition">Admin</Link>
            </div>
        </>
    )
}
