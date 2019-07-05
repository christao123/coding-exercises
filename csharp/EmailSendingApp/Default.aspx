<%@ Page Title="Home Page" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="WebApplication_._Default" %>

<asp:Content ID="BodyContent" ContentPlaceHolderID="MainContent" runat="server">
    <asp:Image class="center-block" src="Images/logo-circle.png" ID="Banner" runat="server" />
    <div align="center">
        <!-- Get started Button-->
        <asp:Button ID="GetStarted" OnCommand="GetStarted_click" Style="margin-left: auto; margin-right: auto;" class="button" runat="server" BorderStyle="Solid" EnableViewState="False" Text="GET STARTED " ToolTip="on you go, chop chop" />
        <!-- Send Email Form-->
        <!--TODO: MAKE EMAILS CONFIGURABLE and remove placeholders-->
        <div class="row">
            <div class="center-block form-group" style="margin-left: auto; margin-right: auto;" id="EmailForm" runat="server">
                <h3>Recipients' address</h3>
                <br />
                <div>
                    <asp:CheckBoxList ID="FromAddressCheckBoxList" runat="server">
                        <asp:ListItem Value="delbussodanilo98@gmail.com"></asp:ListItem>
                        <asp:ListItem Value="Robbie.Lambert@BMWGroup.com"></asp:ListItem>
                        <asp:ListItem Value="Andreas.M.Geier@bmwgroup.com"></asp:ListItem>
                        <asp:ListItem Value="Adrian.Radulescu@BMWGroup.com"></asp:ListItem>
                    </asp:CheckBoxList>
                    <br />
                    <div id="AddImageButtonDiv" runat="server">
                        <asp:TextBox class="form-control" ID="AddEmailTextBox" runat="server" Visible="True"></asp:TextBox>
                        <asp:ImageButton ID="AddEmailImageButton" Style="width: 3%;" src="/Images/email-add-colored.png" runat="server" OnClick="AddEmailImageButton_Click" />
                    </div>
                </div>
                <br />
                <hr />
                <h3>Subject</h3>
                <asp:TextBox Style="width: 100%" class="form-control" ID="SubjectTextBox" Value="File Created in C#" runat="server"></asp:TextBox>
                <br />
                <h3>Message</h3>

                <asp:TextBox Style="width: 100%" class="form-control" ID="MessageTextBox" Value="Hello (Name), here is the text file. Regards (Name)" runat="server" Rows="1" TextMode="MultiLine" Height="150px"></asp:TextBox>
                <br />
                <label>Select Attachments</label>

                <asp:FileUpload Width="1000px" ID="FileUpload" runat="server" AllowMultiple="True" />
                <br />
                <asp:Button ID="SendEmail" OnCommand="SendEmail_click" Style="margin-left: auto; margin-right: auto;" class="button" runat="server" BorderStyle="Solid" EnableViewState="False" Text="Send" ToolTip="on you go, chop chop" />
            </div>
        </div>
    </div>

</asp:Content>
