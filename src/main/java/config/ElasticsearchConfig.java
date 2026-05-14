package config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;

import org.elasticsearch.client.RestClient;

import javax.net.ssl.SSLContext;

public class ElasticsearchConfig {

    private static ElasticsearchClient client;

    public static ElasticsearchClient getClient() {

        if(client == null) {

            try {

                BasicCredentialsProvider credentialsProvider =
                        new BasicCredentialsProvider();

                credentialsProvider.setCredentials(
                        AuthScope.ANY,
                        new UsernamePasswordCredentials(
                                "elastic",
                                "xQBMH1=O+1KoJPJUaE2X"
                        )
                );

                SSLContext sslContext =
                        SSLContextBuilder
                                .create()
                                .loadTrustMaterial(
                                        null,
                                        (certificate, authType) -> true
                                )
                                .build();

                RestClient restClient =
                        RestClient.builder(
                                        new HttpHost(
                                                "localhost",
                                                9200,
                                                "https"
                                        )
                                )
                                .setHttpClientConfigCallback(
                                        httpClientBuilder ->

                                                httpClientBuilder
                                                        .setSSLContext(
                                                                sslContext
                                                        )
                                                        .setSSLHostnameVerifier(
                                                                NoopHostnameVerifier.INSTANCE
                                                        )
                                                        .setDefaultCredentialsProvider(
                                                                credentialsProvider
                                                        )
                                )
                                .build();

                RestClientTransport transport =
                        new RestClientTransport(
                                restClient,
                                new JacksonJsonpMapper()
                        );

                client =
                        new ElasticsearchClient(
                                transport
                        );

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return client;
    }
}