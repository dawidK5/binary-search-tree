# Binary Search Tree (Maven-Compatible)
This Java package allows to generate and manage binary search trees for faster traversal and storing your data of any Comparable type. It performs JUnit 5 tests and it also supports Maven.
This package allows trees to be converted to preorder, postorder and inorder lists, adding node values to TreeSet

## Installation
1. Use [Maven3](https://maven.apache.org/download.cgi#) to build up-to-date JAR file first:
``` 
mvn clean install
```
2. Add the JAR file (located at target/ ) as a dependency/library thorough your IDE e.g. [IntelliJ](https://www.jetbrains.com/help/idea/working-with-module-dependencies.html#add-a-new-dependency) or [Eclipse](https://stackoverflow.com/questions/3280353/how-to-import-a-jar-in-eclipse).
3. Add the following line to your Java project and rebuild it:
```
import org.example.binarysearchtree.BinarySearchTree; 
```

## Use
A typical binary search tree can be declared like this:
```
BinarySearchTree<Integer> myTree = new BinarySearchTree<Integer>();
```
If you want to read full documention run ``` mvn -f pom.xml ``` to generate detailed JavaDoc under target/apidocs/.
