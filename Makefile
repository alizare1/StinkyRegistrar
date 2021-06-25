BUILD_DIR = build
SRC_DIR = src/domain
TEST_DIR = test/domain

TEST_LIBS = lib/hamcrest-all-1.3.jar:lib/junit.jar

JCFLAGS = -O -d $(BUILD_DIR) -classpath $(TEST_LIBS)
JC = javac

JRFLAGS = -classpath $(TEST_LIBS):build
JR = java

CLASSES = \
	$(SRC_DIR)/exceptions/EnrollmentRulesViolationException.java \
	$(SRC_DIR)/exceptions/StudentNotFoundException.java \
	$(SRC_DIR)/exceptions/UnknownOfferingsException.java \
	$(SRC_DIR)/Course.java \
	$(SRC_DIR)/Offering.java \
	$(SRC_DIR)/EnrollCtrl.java \
	$(SRC_DIR)/Student.java \
	$(SRC_DIR)/Term.java \
	$(TEST_DIR)/EnrollCtrlTest.java \
	$(TEST_DIR)/TestRunner.java

all: $(BUILD_DIR) classes

classes: $(CLASSES)
	$(JC) $(JCFLAGS) $(CLASSES)

test:
	@$(JR) $(JRFLAGS) domain.TestRunner

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

.PHONY: test clean
clean:
	rm -f $(BUILD_DIR)/*.class
	rm -r $(BUILD_DIR)