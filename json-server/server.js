// server.js
const jsonServer = require('json-server')
const server = jsonServer.create()
const router = jsonServer.router('db.json')
const middlewares = jsonServer.defaults()

// Set default middlewares (logger, static, cors and no-cache)
server.use(middlewares)

// Add custom routes before JSON Server router
server.get('/echo', (req, res) => {
  res.jsonp(req.query)
})

// To handle POST, PUT and PATCH you need to use a body-parser
// You can use the one used by JSON Server
server.use(jsonServer.bodyParser)
server.use((req, res, next) => {
  if (req.method === 'POST') {
        if(!req.body.name ||  !req.body.surname || !req.body.age || !req.body.role){
            res.status(400).jsonp({error: "Please enter all required fields."})
        }
        if(!(req.body.role === "STUDENT" || req.body.role === "LECTURER" || req.body.role === "OFFICER")){
            res.status(400).jsonp({error: "Please give valid role." })
        }
        if(req.body.age < 18){
            res.status(400).jsonp({error: "Age should be at least 18." })
        }
  }
  // Continue to JSON Server router
  next()
})

// Use default router
server.use(router)
server.listen(3000, () => {
  console.log('JSON Server is running')
})