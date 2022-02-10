const mysql = require('mysql');

const connection = mysql.createConnection({
  host: process.env.HOST,
  user: process.env.USERNAME,
  password: process.env.PASSWORD,
  database: process.env.DB_NAME
});

// DATABASE QUERY
function dbQuery(sql) {
  return new Promise((resolve) => {
    
    connection.query(sql, (error, rows) => {
      if(error) {
        throw error;
      } else {
        console.log('Connection OK, query = ');
        var query;
        query = rows[0];
        resolve(query);
      }
    });
    connection.end();
  });
}

exports.handler = async (event, context) => {

  let sub = event.request.userAttributes.sub;
  let username = event.userName;
  
  let sql = `INSERT INTO user (cognito_id, username) VALUES ('${sub}', '${username}')`;

  await dbQuery(sql);
  
  return event;
};