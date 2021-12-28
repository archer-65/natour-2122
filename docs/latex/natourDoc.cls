\LoadClass[a4paper,12pt]{article} % Classe documento

%--------------------- Packages ------------------------

\RequirePackage[italian]{babel} %Lingua documento
\RequirePackage[utf8]{inputenc} %Caratteri speciali
\RequirePackage[section]{placeins}%Posizione sezione
\RequirePackage[T1]{fontenc} %Alcune lettere che non sono incluse in UTF-8
\RequirePackage{mathtools} %Per simboli ed equazioni
\RequirePackage{float} %Per la posizione delle immagini
\RequirePackage{graphicx} %Per inserire le immaginid
\RequirePackage[justification=centering]{caption} %Per le legende centrate
\RequirePackage{subcaption}
\RequirePackage[table, dvipsnames]{xcolor}
%\RequirePackage[dvipsnames]{xcolor}
\RequirePackage{wallpaper}
\RequirePackage{nomencl}
%\makenomenclature
\RequirePackage{fancyhdr}
%\pagestyle{fancy}
%\fancyheadoffset{1cm}
%\setlength{\headheight}{2cm}
\RequirePackage{url}
\RequirePackage[hidelinks]{hyperref}%Per l'inserimento di legende sotto le immagini
\RequirePackage[left=2.5cm,right=2.5cm,top=2cm,bottom=3.5cm]{geometry} %Configurazione pagina


%-------------------- Info progetto ----------------------

\newcommand{\sottotitolo}[1]{\renewcommand{\sottotitolo}{#1}}
\newcommand{\titolo}[1]{\renewcommand{\titolo}{#1}}
\newcommand{\professors}[1]{\renewcommand{\professors}{#1}}
\newcommand{\students}[1]{\renewcommand{\students}{#1}}

\let\stdsection\section
\renewcommand\section{\newpage\stdsection}

\newcommand{\marginscreate}{
	\makenomenclature
	\pagestyle{fancy}
	\fancyheadoffset{1cm}
	\setlength{\headheight}{2cm}
	\lhead{\includegraphics[scale=0.5]{logos/unina-extended.jpg}} %Logo vicino l'header di ogni pagina
	\rhead{\nouppercase{\leftmark}}
	% \rfoot{\thepage}
	% \cfoot{\textbf{\titolo}}
	% \lfoot{Progettazione}
}

\newcommand{\cover}{
	\begin{titlepage}

	\ThisLRCornerWallPaper{0.6}{logos/SteinECL.jpg}
		\centering %Centra il contenuto
		\includegraphics[width=0.2\textwidth]{logos/unina-logo.png}\par\vspace{1cm} %Inserimento logo
		{\scshape\LARGE Università degli Studi di Napoli \\ Federico II \par} %Nome Università
		\vspace{1.5cm}
		{\scshape\Large \sottotitolo \par}
		\vspace{1cm}
		\rule{\linewidth}{0.2 mm} \\[0.4 cm]
		{\huge\bfseries \titolo \par} \
		\rule{\linewidth}{0.2 mm} \\[1.5 cm]
		\vspace{1cm}
		
		\begin{minipage}{0.5\textwidth} %Minipage per gli studenti
			\begin{flushleft} \large
			\emph{\textbf{Studenti}}\\
			\students\\
			\end{flushleft}
		\end{minipage}
		~
		\begin{minipage}{0.4\textwidth}
			\begin{flushright} \large
			\emph{\textbf{Docenti}} \\
			\professors\\
			\end{flushright}
		\end{minipage}\\[4cm]
		
		\vfill
		{\large A.A. 2021/2022\par}

	\end{titlepage}
}

\newcommand{\toc}{
	\tableofcontents
	\newpage
}

\newcommand{\insertfigure}[4]{
	\begin{figure}[ht]
	\centering
	\includegraphics[height=#2]{#1}
	\caption{#3}
	\label{fig: #4}
	\end{figure}
}
