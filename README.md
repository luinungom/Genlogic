<img alt="Java" align="left"  src="https://forthebadge.com/images/badges/made-with-java.svg"/> <img alt="Java" align="left"  src="https://forthebadge.com/images/badges/built-with-science.svg"/> <img alt="Java" align="left"  src="https://forthebadge.com/images/badges/powered-by-black-magic.svg"/> 
<br/><br/>

<img alt="Logo" align="center" width="1000px" src="https://raw.githubusercontent.com/luinungom/Genlogic/master/src/resources/images/FullLogo.png?raw=true"/>

<img alt="Screenshot" align="center" width="1000px" src="https://raw.githubusercontent.com/luinungom/Genlogic/master/Main%20window.JPG?raw=true"/>

Genlogic is able to:
- Read FASTA and multi-FASTA DNA sequence files.
- Analyze linear or circular (plasmids, vectors) DNA sequences.
- Use endonucleases that cleave within a recognition site.
- Use endonucleases that cleave short specific distances from a recognition site.
- Use endonucleases that cleave at sites remote from a recognition site.
- Use endonucleases with degenerated recognition site (one or more base pairs that are not specifically defined).
- Detect targets in leading strand 5'->3' and lagging strand 3'->5'.
- Allows the user to insert or delete endonucleases manually.
- Results can be exported as txt or CSV files.

#  Genlogic's Structure

```text
Genlogic/
├── .classpath
├── .project
├── build.xml
├── ENDONUCLEASES.dat
├── LICENSE
├── Main window.JPG
├── manifest.mf
├── README.md
├── nbproject/
│   ├── build-impl.xml
│   ├── genfiles.properties
│   ├── jfx-impl.xml
│   ├── project.properties
│   ├── project.xml
│   └── configs/
│   └── private/
├── src/
│   ├── genlogic/
│   │   ├── model/
│   │   │   ├── DNASequence.java
│   │   │   ├── Endonuclease.java
│   │   │   ├── RegexEndonuclease.java
│   │   │   └── RestrictionSite.java
│   │   ├── view/
│   │   │   ├── GenlogicAboutView.fxml
│   │   │   ├── GenlogicAboutViewController.java
│   │   │   ├── GenlogicAddRestrictionEnzymesView.fxml
│   │   │   ├── GenlogicAddRestrictionEnzymesViewController.java
│   │   │   ├── GenlogicDeleteRestrictionEnymesView.fxml
│   │   │   ├── GenlogicDeleteRestrictionEnymesViewController.java
│   │   │   ├── GenlogicMainView.fxml
│   │   │   ├── GenlogicMainViewController.java
│   │   │   ├── GenlogicResultsView.fxml
│   │   │   ├── GenlogicResultsViewController.java
│   │   │   ├── SplashScreen.fxml
│   │   │   └── SplashScreenController.java
│   │   ├── EndonucleaseSerializator.java
│   │   ├── FASTASequenceReader.java
│   │   ├── Genlogic.java
│   │   └── RestrictionSiteSerializator.java
│   └── resources/
│       └── images/
│           ├── FullLogo.png
│           ├── Genlogic Beta.ico
│           └── SimpleLogo.png
```
