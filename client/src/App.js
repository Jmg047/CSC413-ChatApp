import './App.css';
import React from 'react';
import Cookies from 'universal-cookie';
import SearchBar from './component/searchbar.js';


const cookies = new Cookies();

function App() {
  const [userName, setUserName] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [favId, setFavUser] = React.useState(''); // new state variable for favorite user
  const [clickedFav,setFav] = React.useState(false); 
  const [isLoading, setIsLoading] = React.useState(false);
  const [isLoggedIn, setIsLoggedIn] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState('');


  const [favoriteUsers, setFavoriteUsers] = React.useState('');



  // new state variables for chat box
  const [toId, setToId] = React.useState('');
  const [message, setMessage] = React.useState('');

  // new state variable for list of convos
  const [conversations, setConversations] = React.useState([]); // default empty array
  //new state variable for a single conversation
  const [conversation,setConversation] = React.useState([]);

  async function sendAndSeeMessages(){
    await handleSendMessage();
    await handleGetMostRecentConversation();
  }

  async function getConversations() {
    const httpSettings = {
      method: 'GET',
      headers: {
        auth: cookies.get('auth'), // utility to retrive cookie from cookies
      }
    };
    const result = await fetch('/getConversations', httpSettings);
    const apiRes = await result.json();
    console.log(apiRes);
    if (apiRes.status) {
      // worked
      setConversations(apiRes.data); // java side should return list of all convos for this user
    } else {
      setErrorMessage(apiRes.message);
    }
  }

  async function handleSubmit() {
    setIsLoading(true);
    setErrorMessage(''); // fresh error message each time
    const body = {
      userName: userName,
      password: password,
    };
    const httpSettings = {
      body: JSON.stringify(body),
      method: 'POST'
    };
    const result = await fetch('/createUser', httpSettings);
    const apiRes = await result.json();
    console.log(apiRes);
    if (apiRes.status) {
      // user was created
      // todo
    } else {
      // some error message
      setErrorMessage(apiRes.message);
    }
    setIsLoading(false);
  };

  async function handleLogIn() {
    setIsLoading(true);
    setErrorMessage(''); // fresh error message each time
    const body = {
      userName: userName,
      password: password,
    };
    const httpSettings = {
      body: JSON.stringify(body),
      method: 'POST'
    };
    const result = await fetch('/login', httpSettings);
    if (result.status === 200) {
      // login worked
      setIsLoggedIn(true);
      getConversations();
    } else {
      // login did not work
      setErrorMessage(`Username or password incorrect.`);
    }

    setIsLoading(false);
  };



  async function handleGetMostRecentConversation(){
    setIsLoading(true);
    const httpSettings = {
      method: 'GET',
      headers: {
        auth: cookies.get('auth'), // utility to retrive cookie from cookies
      },
    };
    const result = await fetch('/getMostRecentConversation', httpSettings);
    console.log(result);
    const apiRes = await result.json();
    console.log(apiRes);
    if (apiRes.status) {
      // worked
      setConversation(apiRes.data); // java side should return list of most recent convo for this user
    } else {
      setErrorMessage(apiRes.message);
    }
    setIsLoading(false);
  };




  async function handleSendMessage() {
    setIsLoading(true);
    setErrorMessage(''); // fresh error message each time
    const body = {
      fromId: userName,
      toId: toId,
      message: message,
    };
    const httpSettings = {
      body: JSON.stringify(body),
      method: 'POST',
      headers: {
        auth: cookies.get('auth'), // utility to retrive cookie from cookies
      }
    };
    const result = await fetch('/createMessage', httpSettings);
    const apiRes = await result.json();
    console.log(apiRes);
    if (apiRes.status) {
      // worked
      setMessage('');
      await getConversations();
    } else {
      setErrorMessage(apiRes.message);
    }
    setIsLoading(false);
  };

  async function  addFavoriteUser() {
    setFavUser(''); // Clear the input field
    setIsLoading(true);
    setErrorMessage('');
    const body = {
      userName: userName,
      favId: favId,
    };
    const httpSettings = {
      body: JSON.stringify(body),
      method: 'POST',
      headers: {
        auth: cookies.get('auth'), // utility to retrive cookie from cookies
      }
    };
    const result = await fetch('/createFavList', httpSettings);
    const apiRes = await result.json();
    console.log(apiRes);
    if (apiRes.status) {
      // worked
      setMessage('');
      await getConversations();
    } else {
      setErrorMessage(apiRes.message);
    }
    setIsLoading(false);
  };

  async function search(searchTerm) { 
    setIsLoading(true);

    const httpSettings = {
      method: 'GET',
      headers: {
        auth: cookies.get('auth'),
      },
    };

    const result = await fetch(`/SearchBar?search=${encodeURIComponent(searchTerm)}`, httpSettings);
    console.log(result);

    const apiRes = await result.json();
    console.log(apiRes);

    if (apiRes.status) {
      setConversation(apiRes.data);
    } else {
      setErrorMessage(apiRes.message);
    }

    setIsLoading(false);
  }


  if (isLoggedIn) {
    return (
        <div className="App">
          <div className="bar">
            <SearchBar/>
            <button onClick={search} className="submit" type="submit">Search</button>
          </div>
          <h1 className="banner">{<text style={{margin:"20px"}}>Welcome, {userName}!</text>}</h1>
          <div className="inputArea">
            <div className="inputBox" style={{backgroundColor: "transparent"}}>
              {<text style={{color: "white", fontSize: "1.5em"}}>To: </text>}
              <input className="inputBox" value={toId} onChange={e => setToId(e.target.value)} />
            </div>
            <textarea className="inputBox" value={message} onChange={e => setMessage(e.target.value)} />
            <div>
              <button onClick={sendAndSeeMessages} className="buttonBox">Send Message</button>
              <button onClick={handleGetMostRecentConversation} className="buttonBox">Resume Most Recent conversation</button>

              <div className="inputArea">
              <input
                style={{backgroundColor:"white"}}
                className="inputBox"
                value={favId}
                onChange={e => setFavUser(e.target.value)}
                placeholder="Enter username"
              />
              <button onClick={addFavoriteUser} className="buttonBox">
                Add Favorite User
              </button>
              </div>

            </div>
          </div>
         <div style={{ color: 'red' }}>{errorMessage}</div>

          <div className="convoArea">{conversations.map(conversation =>
              <div className="indivConvo">Convo: {conversation.conversationId}</div>)}
          </div>
          <div className="content">
          <div className="convoArea">{conversation.map(conversation =>
                  <div>{(conversation.fromId === userName) ?
                      (<div className="indivConvoFrom">{conversation.fromId}:  {conversation.message}</div>):
                      (<div className="indivConvoFrom">{conversation.fromId}:  {conversation.message}</div>)}
                  </div>)}

          </div>
        </div>
     </div>
    );
  }

  return (
    <div className="App">
      <div className="inputArea">
        <label htmlFor="username" style={{color:"white"}}>Username</label>
        <div>
          <input id="username" className="inputBox" value={userName} onChange={e => setUserName(e.target.value)} />
        </div>
        <label htmlFor="password" style={{color:"white"}}>Password</label>
        <div>
          <input id="password" className="inputBox" value={password} onChange={e => setPassword(e.target.value)} type="password" />
        </div>
        <button onClick={handleSubmit} disabled={isLoading} className="buttonBox">Register</button>
        <button onClick={handleLogIn} disabled={isLoading} className="buttonBox">Log in</button>
      </div>
      <div>
        {isLoading ? 'Loading ...' : null}
      </div>
      <div style={{ color: 'red' }}>{errorMessage}</div>
    </div>
  );
}

export default App;