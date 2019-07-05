using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.IO;
using System.Linq;
using System.Net.Mail;
using System.Web;
using System.Web.Mail;
using System.Web.UI;
using System.Web.UI.WebControls;
using WebApplication_.CustomUtils;


namespace WebApplication_
{
    public partial class _Default : Page
    {
        private readonly string _rootPath = HttpContext.Current.Server.MapPath("~");
        private EmailSender emailSender;
        private readonly string _attachmentsPath = "/App_Data/Attachments/";
        private bool addingEmailAddress = false;

        protected void Page_Load(object sender, EventArgs e)
        {

            EmailForm.Visible = false;
            // AddEmailTextBox.Visible = false;
            //addingEmailAddress = false;

            //todo: check if this stuff goes here or not
            Page.Title = "Dan's ";
            string t = new FileManager().ReadFromFile(_rootPath + "/App_Data/file.txt");


            string bigString = "Imma Win That Coffee\n";
            for (int i = 0; i < 10; i++)
            {
                bigString += bigString;
            }


            emailSender = new EmailSender(_rootPath + "App_Data/configs/defaultEmailCfg.xml");
        }

        protected void GetStarted_click(object sender, EventArgs e)
        {
            GetStarted.Visible = false;
            EmailForm.Visible = true;
        }

        protected void SendEmail_click(object sender, EventArgs e)
        {
            ListItemCollection toAddresses = FromAddressCheckBoxList.Items;
            string subject = SubjectTextBox.Text;
            string body = MessageTextBox.Text;


            IList<HttpPostedFile> postedFiles = FileUpload.PostedFiles;


            ArrayList attachments = GenerateAttachments(postedFiles);


            //add hardcoded custom attachment
            new FileManager().WriteToFile(
                _rootPath + "/App_Data/new_file.txt",
                body);

            Attachment customFile = new Attachment(_rootPath + "/App_Data/new_file.txt");
            customFile.Name = "new_file.txt";
            attachments.Add(customFile);


            //todo: remove alert and use an actual message popup and maybe make the "Choose the recipient" message modular?

            foreach (ListItem address in toAddresses)
            {
                string toAddress = address.Value;

                if (IsEmail(toAddress) && address.Selected)
                {

                    if (!emailSender.SendEmail(toAddress, subject,
                        body, attachments))
                    {

                        Response.Write("<script>alert('There was an error. Email was not sent to " + toAddress + "')</script>");
                    }
                    else
                    {
                        //delete attachments from server
                        foreach (var fileUpload in postedFiles)
                        {
                            string attachmentPath = Path.GetFileName(fileUpload.FileName);
                            try
                            {
                                //todo: this isnt working. no files are deleted
                                File.Delete(Server.MapPath(_rootPath + _attachmentsPath + attachmentPath));
                            }
                            catch (Exception ex)
                            {


                                Console.WriteLine("Could not delete attachment on server. {0}", ex.Message);
                            }


                        }
                        Response.Write("<script>alert('EMAIL HAS BEEN SENT TO " + toAddress + "')</script>");
                    }
                }
            }
        }

        private ArrayList GenerateAttachments(IList<HttpPostedFile> postedFiles)
        {
            ArrayList attachments = new ArrayList();
            //we will use these paths to delete the attachments from the server once the email is sent

            foreach (var postedFile in postedFiles)
            {
                if (postedFile != null)
                {
                    /* Get a reference to PostedFile object */
                    HttpPostedFile attFile = postedFile;
                    /* Get size of the file */
                    int attachFileLength = attFile.ContentLength;
                    /* Make sure the size of the file is > 0  */
                    if (attachFileLength > 0)
                    {
                        /* Get the file name */
                        string strFileName = Path.GetFileName(postedFile.FileName);
                        /* Save the file on the server */
                        postedFile.SaveAs(Server.MapPath(_attachmentsPath + strFileName));
                        /* Create the email attachment with the uploaded file */
                        Attachment attach = new Attachment(Server.MapPath(_attachmentsPath + strFileName));
                        /* Attach the newly created email attachment */
                        attachments.Add(attach);
                        /* Store the attach filename so we can delete it later */
                    }
                }
            }

            return attachments;
        }

        //---------------------------------------------HELPER FUNCTIONS------------------------------------------------------
        /// <summary>
        /// Check if string is a properly formatted email address
        /// </summary>
        /// <param name="email">the string to check</param>
        /// <returns>true if email is an email address</returns>
        private bool IsEmail(string email)
        {
            try
            {
                var address = new System.Net.Mail.MailAddress(email);
                return address.Address == email;
            }
            catch
            {
                return false;
            }
        }

        protected void AddEmailImageButton_Click(object sender, ImageClickEventArgs imageClickEventArgs)
        {
            if (IsEmail(AddEmailTextBox.Text))
            {
                //todo:add new address
            }
        }

    }
}