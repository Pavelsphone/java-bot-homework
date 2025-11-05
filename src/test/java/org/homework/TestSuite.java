package org.homework;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("org.homework")
@IncludeClassNamePatterns(".*Test")
public class TestSuite {
}


