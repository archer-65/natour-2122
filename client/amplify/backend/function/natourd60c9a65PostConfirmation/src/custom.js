// import { createConnection } from 'mysql';

// var connection = createConnection({
//   host: "localhost",
//   user: "yourusername",
//   password: "yourpassword"
// })

// export function handler(event, context, callback) {

//   connection.connect();

//   try {

//     // Get the sub of the Cognito user
//     if (!event.request.userAttribute.sub) {
//       // No UUID
//       throw "No UUID available";
//     }
    
//     const sub = event.request.userAttribute.sub;
//     const username = event.request.userAttribute.username;

//     connection.query(`INSERT INTO user (cognito_id, username) VALUES ('${sub}', '${username}')`);
//   } catch (error) {
//     console.log(error);
//   }

//   connection.end();
//   context.done(null, event);
// }