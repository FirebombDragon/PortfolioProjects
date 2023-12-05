const jwt = require('jsonwebtoken');

const TOKEN_COOKIE_NAME = "CookEToken";
const API_SECRET = "2z3u9z3945h1kzpfctzbk2oc3b6fb2p1";

exports.TokenMiddleware = (req, res, next) => {
  if(!req.cookies[TOKEN_COOKIE_NAME]) { // If we don't have a token
    res.status(401).json({error: 'Not authenticated'});
    return;
  } 
  let token = req.cookies[TOKEN_COOKIE_NAME];

  try {
    const decoded = jwt.verify(token, API_SECRET);
    req.user = decoded.user;
    next();
  }
  catch(err) { //Token is invalid
    res.status(401).json({error: 'Not authenticated'});
    return;
  }
}

exports.generateToken = (req, res, user) => {
  let data = {
    user: user,
    exp: Math.floor(Date.now() / 1000) + (60 * 60)
  }

  const token = jwt.sign(data, API_SECRET);

  //send token in cookie to client
  res.cookie(TOKEN_COOKIE_NAME, token, {
    httpOnly: true,
    secure: true,
    maxAge: 5 * 60 * 1000
  });
};


exports.removeToken = (req, res) => {
  //send session ID in cookie to client
  res.cookie(TOKEN_COOKIE_NAME, "", {
    httpOnly: true,
    secure: true,
    maxAge: -360000 //A date in the past
  });

}