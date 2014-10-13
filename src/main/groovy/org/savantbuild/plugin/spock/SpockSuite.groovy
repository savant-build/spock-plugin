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

  Set<String> tests = new TreeSet<>()

  boolean initialized

  Output output

  String spockExtension = "Spec.groovy"

  String sourceTestDirectory = "src/test/groovy"

  private findSpecifications() {
    Files.walkFileTree( Paths.get(sourceTestDirectory), new SimpleFileVisitor<Path>() {

      @Override
      public FileVisitResult visitFile(Path filePath, BasicFileAttributes attributes) {
        if (filePath != null) {
          output.debug(filePath.fileName.toString());
          if (filePath.fileName.toString().endsWith(spockExtension)) {
            output.debug(filePath.toString())
            tests.add(filePath.toString())
          }
        }
        return FileVisitResult.CONTINUE
      }
    })
  }

  void initialize() {
    findSpecifications()
    initialized = true
  }

}
