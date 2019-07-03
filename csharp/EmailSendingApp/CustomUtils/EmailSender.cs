using System;
using System.Net.Mail;
using System.Web;

namespace WebApplication_.CustomUtils
{
    /// <summary>
    /// Send Emails
    /// </summary>
    public class EmailSender
    {
        public EmailSenderConfiguration EmailCfg { get; set; }

        public EmailSender(string xmlConfigFilePath)
        {
            EmailCfg = new EmailSenderConfiguration(xmlConfigFilePath);
        }
        /// <summary>
        ///  Send an email according to the content of the configuration file
        /// </summary>
        /// <param name="toAddress">the receiver's email address</param>
        /// <param name="subject">the subject of the email</param>
        /// <param name="body">the main content of the email</param>
        /// <param name="attachmentPaths">an array containing the attachments' paths</param>
        public void SendEmail(string toAddress, string subject, string body, string[] attachmentPaths)
        {
            try
            {
                MailMessage mail = new MailMessage();
                SmtpClient SmtpServer = new SmtpClient(EmailCfg.SmtpServer);
                mail.From = new MailAddress(EmailCfg.FromAddress);
                mail.To.Add(toAddress);
                mail.Subject = subject;
                mail.Body = body;
                
                System.Diagnostics.Debug.WriteLine(EmailCfg.EnableSsl);
                SmtpServer.Port = EmailCfg.Port;
                SmtpServer.Credentials = new System.Net.NetworkCredential(EmailCfg.Username, EmailCfg.Password);
                SmtpServer.EnableSsl = EmailCfg.EnableSsl;

                foreach (var path in attachmentPaths)
                {
                    //todo: check if attachment file path exists
                    mail.Attachments.Add(new Attachment(path));
                }

                SmtpServer.Send(mail);
            }
            catch (Exception e)
            {
                Console.WriteLine("Failed sending email. \n{0}", e.Message);
            }

        }
    }
}
