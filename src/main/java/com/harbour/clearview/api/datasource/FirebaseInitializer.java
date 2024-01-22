package com.harbour.clearview.api.datasource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.io.InputStream;

public class FirebaseInitializer {
    public static Firestore initialize() throws IOException {
        try (InputStream serviceAccount = FirebaseInitializer.class.getClassLoader().getResourceAsStream("serviceAccountKey.json")) {
            if (serviceAccount == null) throw new RuntimeException();
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);
            return FirestoreClient.getFirestore();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}