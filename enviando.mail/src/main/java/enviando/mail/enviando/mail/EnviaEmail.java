package enviando.mail.enviando.mail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;


public class EnviaEmail {
	private String username = "emaildeteste@gmail.com";
	private String senha = "senhadoemaildeteste";
	private String listaDestinatarios = null;
	private String nomeRemetente = null;
	private String assuntoEmail = null;
	private String textoEmail = null;
	
	public EnviaEmail(String ListaDestinatario, String NomeRemetente, String AssuntoEmail, String TextoEmail) {
		this.listaDestinatarios = ListaDestinatario;
		this.nomeRemetente = NomeRemetente;
		this.assuntoEmail = AssuntoEmail;
		this.textoEmail = TextoEmail;
	}
	
    public void EnviarEmail(boolean envioHtml, boolean envioAnexo) {
		try {
			/*Cria as propiedades do Serviço de envio*/
			Properties properties = new Properties();
			
			properties.put("mail.smtp.auth", "true");/*Autorização*/
			properties.put("mail.smtp.starttls", "true");/*Autenticação TSL*/
			properties.put("mail.smtp.ssl.trust", "*");/*Autenticação SSl*/
			properties.put("mail.smtp.host", "smtp.gmail.com"); /*Sercidor gmail Google*/
			properties.put("mail.smtp.port", "465");/*Porta do servidor*/
			properties.put("mail.smtp.socketFactory.port", "465");/*Expecifica a porta a ser conectada pelo socket*/
			properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");/*Classe socket de conexão ao SMTP*/
			
			Session session = Session.getInstance(properties, new Authenticator() {/*Faz a autenticação com o email*/
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, senha);
				}
			});
			
			Address[] toUser = InternetAddress.parse(listaDestinatarios);/*Lista de emails destinatário*/
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username, nomeRemetente));/*Quem está enviando*/
			message.setRecipients(Message.RecipientType.TO, toUser);/*Emails de destino*/
			message.setSubject(assuntoEmail);/*Assunto do email*/
			
			if(envioAnexo) {/*Se for envio com anexo*/
				/*Parte 1- CORPO do email-------------------------------------*/
				MimeBodyPart corpoEmail = new MimeBodyPart();
				
				if(envioHtml) { /*Se o tipo de email for HTML ele seta o Content para html*/
					corpoEmail.setContent(textoEmail, "text/html; charset=utf-8");
				}else { /*Se não, envia um texto puro*/
					corpoEmail.setText(textoEmail);/*Conteúdo do Email*/
				}
				
				/*Parte 2- ANEXO do email------------------------------------*/
				MimeBodyPart anexoEmail = new MimeBodyPart();
				
				/*Pode ser pegado do banco*/
				String arquivoBase64 = "";/*Aqui vai o Base64 do arquivo*/
				String contenttype =  "";/*Aqui vai o contenttype do arquivo*/
				
				anexoEmail.setDataHandler(new DataHandler(new ByteArrayDataSource(arquivoBase64, contenttype)));
				anexoEmail.setFileName("arquivo.txt");/*Pode generalizar colocando a extensão com um split no contenttype*/
				
				/*Junta as partes Corpo e Anexo-------------------------------*/
				Multipart multipart = new MimeMultipart();/*Cria um Objeto MultiPart*/			
				multipart.addBodyPart(corpoEmail);/*Junto o corpo ao objeto multipart*/
				multipart.addBodyPart(anexoEmail);/*Junto o anexo ao objeto multipart*/
				
				message.setContent(multipart);/*Seta o Multiparte á message*/
				
				
				
				
				
				
				
				/*INICIO Variante do código com multiplos anexos ..............................*/
				
				/*Parte 2- ANEXO do email------------------------------------*/
				//MimeBodyPart anexoEmail = new MimeBodyPart();
				
				
				//Multipart multipart = new MimeMultipart();/*Cria um Objeto MultiPart*/				
				//multipart.addBodyPart(corpoEmail);/*Junto o corpo ao objeto multipart*/
				
				//List<String> arquivosBase64 = new ArrayList<String>();/*Adiciona os arquivos em base64 na lista arquivosBase64*/
				//arquivosBase64.add(daoUsuario.buscarArquivoDoUsuario("1"));/*Busca do banco a base64 gravada no formato: data:image/png;base64,iVBORw0K....*/
				//arquivosBase64.add(daoUsuario.buscarArquivoDoUsuario("5"));
				//arquivosBase64.add(daoUsuario.buscarArquivoDoUsuario("14"));
				
				//int index = 0;
				
				//for (String arquivoBase64 : arquivosBase64) {
				//	String base64 = arquivoBase64.split(",")[1];/*retorna a base64 do arquivo, ex: iVBORw0K....*/
				//	String typeFile = arquivoBase64.split(";")[0].split("/")[1];/*retorna o tipo do arquivo, ex:png*/
				//	anexoEmail.setDataHandler(new DataHandler(new ByteArrayDataSource(base64, typeFile)));
				//	anexoEmail.setFileName("arquivo"+index+"."+typeFile);/*Pode generalizar colocando a extensão com um split no contenttype*/
					
				//	multipart.addBodyPart(anexoEmail);/*Junto o anexo ao objeto multipart*/
				//	index++;
				//}

				/*Junta as partes Corpo e Anexo na Message-----------------------*/
				//message.setContent(multipart);
				
				/*FIM Variante do código com multiplos anexos ................................*/
				
				
				
				
				
				
				
				
			}else {/*Se Não for envio com anexo*/
				if(envioHtml) { /*Se o tipo de email for HTML ele seta o Content para html*/
					message.setContent(textoEmail, "text/html; charset=utf-8");
				}else { /*Se não, envia um texto puro*/
					message.setText(textoEmail);/*Conteúdo do Email*/
				}				
			}
			
			Transport.send(message);/*Executa o envio*/				
			
		}catch (Exception e) {
			e.printStackTrace();
		}
    }
}
