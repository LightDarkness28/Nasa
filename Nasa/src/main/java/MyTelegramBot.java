import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MyTelegramBot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final String URL = "https://api.nasa.gov/planetary/apod?api_key=HdsTBLdeix4r3TzIzFcQZZWzgGrSAjhbAp9orYsq";


    public MyTelegramBot(String BOT_NAME, String BOT_TOKEN) throws TelegramApiException {
        this.BOT_NAME = BOT_NAME;
        this.BOT_TOKEN = BOT_TOKEN;

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String[] separatedAction = update.getMessage().getText().split(" ");

            String action = separatedAction[0];
            long chatId = update.getMessage().getChatId();

            switch (action){
                case "/help":
                    sendMessage("Этот бот присылает картинку дня по запросу /image", chatId);
                    break;
                case "/image":
                case "/start":
                    String image = Utils.getUrl(URL);
                    sendMessage(image,chatId);
                    break;
                case "/date":
                    image = Utils.getUrl(URL + "&date=" + separatedAction[1]);
                    sendMessage(image,chatId);
                    break;
                case "/dateHD":
                    image = Utils.getHDUrl(URL + "&date=" + separatedAction[1]);
                    sendMessage(image,chatId);
                    break;

                default:
                    sendMessage("Мне неизвестна данная команда",chatId);

            }

        }
    }


    void sendMessage (String msg, long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(msg);
        try{
            execute(message);

        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {

        return BOT_NAME;
    }

    @Override
    public String getBotToken() {

        return BOT_TOKEN;
    }

}
