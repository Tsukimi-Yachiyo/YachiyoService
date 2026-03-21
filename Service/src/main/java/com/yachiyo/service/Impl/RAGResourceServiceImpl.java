package com.yachiyo.service.Impl;

import com.yachiyo.result.Result;
import com.yachiyo.service.RAGResourceService;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class RAGResourceServiceImpl implements RAGResourceService {

    @Autowired
    private TokenTextSplitter tokenTextSplitter;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Override
    public Result<Boolean> uploadResource(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            Resource resource = file.getResource();
            TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
            List<Document> documents = tikaDocumentReader.read();
            List<Document> texts = tokenTextSplitter.apply(documents);
            vectorStore.add(texts);
        }
        return Result.success(true);
    }
}
