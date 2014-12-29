/*
 * Copyright (c) 2014, Inversoft Inc., All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.savantbuild.plugin.spock

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

import org.savantbuild.output.Output

/**
 * @author Daniel DeGroff
 */
class SpockSuite {

  private String fileExtension

  Set<String> tests = new TreeSet<>()

  boolean initialized

  Output output

  List singleTests

  String spockSuffix = "Spec.groovy"

  String sourceTestDirectory = "src/test/groovy"

  private findSpecifications() {

    int singleTestsCount = 0
    if (singleTests != null) {
      output.info("Running only the following specifications: [%s].", String.join(", ", singleTests))
      singleTestsCount = singleTests.size()
    }

    Files.walkFileTree( Paths.get(sourceTestDirectory), new SimpleFileVisitor<Path>() {

      @Override
      public FileVisitResult visitFile(Path filePath, BasicFileAttributes attributes) {
        if (filePath != null) {
          if (singleTests != null) {
            String fileName = filePath.getFileName().toString();
            fileName = fileName.substring(0, fileName.indexOf(fileExtension))
            if (singleTests.contains(fileName)) {
              tests.add(filePath.toString())
              singleTestsCount--
              if (singleTestsCount == 0) {
                return FileVisitResult.TERMINATE
              }
            }
          } else {
            if (filePath.fileName.toString().endsWith(spockSuffix)) {
              tests.add(filePath.toString())
            }
          }
        }
        return FileVisitResult.CONTINUE
      }
    })
  }

  void initialize() {

    fileExtension = spockSuffix
    if (spockSuffix.indexOf('.') > 0) {
      fileExtension = spockSuffix.substring(spockSuffix.indexOf('.'))
    }

    findSpecifications()
    initialized = true
  }

}
