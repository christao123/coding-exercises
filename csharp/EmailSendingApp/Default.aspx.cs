using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using WebApplication_.CustomUtils;


namespace WebApplication_
{
    public partial class _Default : Page
    {
        private readonly string _rootPath = HttpContext.Current.Server.MapPath("~");
        protected void Page_Load(object sender, EventArgs e)
        {
           string t = new FileManager().ReadFromFile(_rootPath + "/App_Data/file.txt");
           TextBox1.Text = t;

           string bigString = "Imma Win That Coffee\n";
           for (int i = 0; i < 10; i++)
           {
               bigString += bigString;
           }

           new FileManager().WriteToFile(
               _rootPath + "/App_Data/new_file.txt", 
               bigString);

           string[] attachmentsPaths =
           {
               _rootPath + "App_Data/new_file.txt",
               _rootPath + "App_Data/images/logo.PNG"
           };
           new EmailSender(_rootPath + "App_Data/configs/defaultEmailCfg.xml").SendEmail(
               "delbussodanilo98@gmail.com", "Test Message",
               "this is a message", attachmentsPaths
               );
        }
    }
}