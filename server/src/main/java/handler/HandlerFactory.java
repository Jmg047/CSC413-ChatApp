package handler;

import request.ParsedRequest;

public class HandlerFactory {
  // routes based on the path. Add your custom handlers here
  public static BaseHandler getHandler(ParsedRequest request) {
    switch (request.getPath()) {
      case "/createUser":
        return new CreateUserHandler();
      case "/login":
        return new LoginHandler();
      case "/getConversations":
        return new GetConversationsHandler();
      case "/getConversation":
        return new GetConversationHandler();
      case "/createMessage":
        return new CreateMessageHandler();
      case "/getMostRecentConversation": //endpoint created by Elliot Warren
        return new GetMostRecentConversationHandler();
      case "/createFavList": //endpoint created by Akram Al Raeeini
        return new CreateFavListHandler();
      case "/deleteEverything":
        return new DeleteEverything(); //Conrad Choi made this.
      case "/searchBar":
        return new SearchBarHandler(); // Endpoint made by Jaime G.
     default:
         return new FallbackHandler();
    }
  }

}
