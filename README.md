# jira-reporter

This Java/FX application gathers all *'Ready To Test'* and *'Done'* Atlassian JIRA tasks, from a given SCRUM board for a given assignee, creates a report and sends the report to a recipients list.

Currently I use Gmail SMTP as email service. But you can find MailgunSender inside the code, it is a Mailgun API client. Both GmailSender and MailgunSender implement the same interface, so it's easy to use any sender you want.

This utility has been created for my own needs, but if you need this - take it and use or modify if needed.

Author: Oleg Zaidullin aka Cornknight 
(*cornknight@gmail.com*)