package models;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class BookDataModel {
    String userId;
    List<CollectionOfIsbns> collectionOfIsbns;

    @Data
    public static class CollectionOfIsbns {
        String isbn;
    }

    public static BookDataModel createWithSingleIsbn(String userId, String isbn) {
        BookDataModel model = new BookDataModel();
        model.setUserId(userId);

        CollectionOfIsbns isbnItem = new CollectionOfIsbns();
        isbnItem.setIsbn(isbn);

        model.setCollectionOfIsbns(Collections.singletonList(isbnItem));
        return model;
    }
}