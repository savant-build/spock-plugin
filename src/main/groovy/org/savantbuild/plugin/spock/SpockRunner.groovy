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

import java.nio.file.Path

import org.savantbuild.domain.Project
import org.savantbuild.lang.Classpath
import org.savantbuild.output.Output

/**
 * @author Daniel DeGroff
 */
class SpockRunner {
  String groovyHome

  Path groovyPath

  String javaHome

  Output output

  Project project

  SpockSettings settings

  int doRun(Classpath classpath, SpockSuite suite) {
    if (!suite.initialized) {
      throw new IllegalStateException("SpockSuite must be initialized prior to calling doRun() on the SpockRunner.")
    }

    int result = 0
    suite.tests.each() {
      output.infoln("[Spec] ${it}")

      String command = "${groovyPath} ${settings.jvmArguments} ${classpath.toString("-cp ")} ${it}"
      output.debugln(" Running command: [%s]", command)

      Process process = command.execute(["JAVA_HOME=${javaHome}", "GROOVY_HOME=${groovyHome}"], project.directory.toFile())
      process.consumeProcessOutput((Appendable) System.out, System.err)

      result = process.waitFor()
      if (result != 0) {
        return result;
      }
    }

    return result
  }
}
