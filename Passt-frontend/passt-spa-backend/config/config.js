//Client configuration
const config = {
    oauth: {
        clientId: process.env.passtClient1,
        clientSecret: process.env.passtSecret1,
        redirectUri: "https://localhost:4200/api/callback",
        authServer: 'https://localhost:8443',
        scopes: ['read', 'write']
    },
    frontendUri: "https://localhost:4200"
}

module.exports = config;