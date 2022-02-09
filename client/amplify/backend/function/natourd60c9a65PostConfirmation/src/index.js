const mysql = require('mysql');

const connection = mysql.createConnection({
  host: process.env.HOST,
  user: process.env.USERNAME,
  password: process.env.PASSWORD,
  database: process.env.DB_NAME
});

exports.handler = (event, context, callback) => {

  context.callbackWaitsForEmptyEventLoop = false;

  try {
    // Get the sub of the Cognito user
    if (!event.request.userAttributes.sub || !event.userName) {
      // No UUID or username
      throw "No UUID or username available";
    }
    
    const sub = event.request.userAttributes.sub;
    const username = event.userName;
  
    const sql = `INSERT INTO user (cognito_id, username) VALUES ('${sub}', '${username}')`;

    connection.query(sql, function (err, result) {
      if(err) throw err;
      callback(null, result);
    });
    
  } catch (error) {
    console.log(error);
  }

  context.done(null, event);
};
