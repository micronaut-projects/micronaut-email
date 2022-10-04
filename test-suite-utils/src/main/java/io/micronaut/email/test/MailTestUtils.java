/*
 * Copyright 2017-2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.email.test;

import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;

import java.util.Properties;

/**
 * Utility to read and delete emails from a Gmail inbox. Useful to test Transactional email integration.
 * @author Sergio del Amo
 */
public final class MailTestUtils {
    private final static String HOST = "smtp.gmail.com";
    private final static String FOLDER_INBOX = "Inbox";

    /**
     *
     * @param user SMTP user
     * @param password SMTP password
     * @param subject Target subject
     * @return the number of emails which matched the target subject
     * @throws MessagingException exception thrown while reading emails
     */
    public static int countAndDeleteInboxEmailsBySubject(String user,
                                                         String password,
                                                         String subject) throws MessagingException {
        return countAndDeleteEmailsBySubject(FOLDER_INBOX, HOST, subject, user, password);
    }

    /**
     *
     * @param subject Target subject
     * @return the number of emails which matched the target subject
     * @throws MessagingException exception thrown while reading emails
     */
    public static int countAndDeleteInboxEmailsBySubject(String subject) throws MessagingException {
        String user = System.getenv("GMAIL_USERNAME");
        String password = System.getenv("GMAIL_PASSWORD");
        return countAndDeleteEmailsBySubject(FOLDER_INBOX, HOST, subject, user, password);
    }

    private static int countAndDeleteEmailsBySubject(String folderName,
                                                     String host,
                                                     String subject,
                                                     String user,
                                                     String password)  throws MessagingException {
            Properties prop = createProperties(host);
            Session session = Session.getInstance(prop, null);
            Store store = session.getStore("imaps");
        store.connect(host, user, password);
        Folder folder = store.getFolder(folderName);
        folder.open(Folder.READ_WRITE);
        Message[] messages = folder.getMessages();
        int count = 0;
        for (Message message: messages) {
            if (message.getSubject().equals(subject)) {
                count++;
                message.setFlag(Flags.Flag.DELETED, true);
            }
        }
        folder.close(true);
        store.close();
        return count;
    }

    private static Properties createProperties(String host) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.socketFactory.port","465");
        prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.port","465");
        return prop;
    }
}
