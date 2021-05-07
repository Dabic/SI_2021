package rs.raf.modelvalidator.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rs.raf.modelvalidator.domain.*;
import rs.raf.modelvalidator.exceptions.CustomHttpException;
import rs.raf.modelvalidator.persistance.MetaDataRepository;
import rs.raf.modelvalidator.services.ValidatorService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValidatorServiceImpl implements ValidatorService {

    private final MetaDataRepository metaDataRepository;
    private final Logger logger = LoggerFactory.getLogger(ValidatorServiceImpl.class);

    public ValidatorServiceImpl(MetaDataRepository metaDataRepository) {
        this.metaDataRepository = metaDataRepository;
    }

    @Override
    public void validate(ClassDiagram classDiagram) {
        if (classDiagram.getDNodes() == null)
            return;
        MetaData metaData = getMetaDataByModelType(classDiagram.getModelType());
        if (metaData.getGeneralisationCount() != null) {
            checkGeneralisationCount(classDiagram, metaData.getGeneralisationCount());
        }
        if (metaData.getCheckFieldTypeValidity() != null && metaData.getCheckFieldTypeValidity()) {
            checkFieldTypeValidity(classDiagram);
        }
        if (metaData.getCheckClassNameDuplicates() != null && metaData.getCheckClassNameDuplicates()) {
            checkClassNameDuplicates(classDiagram);
        }
        if (metaData.getCheckAuthorToStoryLinks() != null && metaData.getCheckAuthorToStoryLinks()) {
            checkAuthorToStoryLinks(classDiagram);
        }
        if (metaData.getCheckGeneralisationUsage() != null && metaData.getCheckGeneralisationUsage()) {
            checkGeneralisationUsage(classDiagram);
        }
        if (metaData.getCheckImplementationUsage() != null && metaData.getCheckImplementationUsage()) {
            checkImplementationUsage(classDiagram);
        }
        if (metaData.getCheckIncludeUsage() != null && metaData.getCheckIncludeUsage()) {
            checkIncludeUsage(classDiagram);
        }
        if (metaData.getCheckExtendUsage() != null && metaData.getCheckExtendUsage()) {
            checkExtendUsage(classDiagram);
        }
        if (metaData.getCheckRelationUsage() != null && metaData.getCheckRelationUsage()) {
            checkRelationUsage(classDiagram);
        }
    }

    public void checkRelationUsage(ClassDiagram classDiagram) {
        for (DLink dLink : classDiagram.getDLinks()) {
            if (dLink.getType().equals("")) {
                DNode fromNode = getNodeForKey(classDiagram.getDNodes(), dLink.getFromNode());
                DNode toNode = getNodeForKey(classDiagram.getDNodes(), dLink.getToNode());
                if (!fromNode.getType().equals(DNodeType.Actor.name()) || !toNode.getType().equals(DNodeType.Ellipse.name())) {
                    throw new CustomHttpException(
                            String.format("Can't use relation link between %s %s!", fromNode.getText(), toNode.getText()),
                            HttpStatus.BAD_REQUEST);
                }
            }
        }
    }

    public void checkExtendUsage(ClassDiagram classDiagram) {
        for (DLink dLink : classDiagram.getDLinks()) {
            if (dLink.getType().equals(DLinkType.OpenTriangleLine.name())) {
                DNode fromNode = getNodeForKey(classDiagram.getDNodes(), dLink.getFromNode());
                DNode toNode = getNodeForKey(classDiagram.getDNodes(), dLink.getToNode());
                if (!fromNode.getType().equals(DNodeType.Ellipse.name()) || !toNode.getType().equals(DNodeType.Ellipse.name())) {
                    throw new CustomHttpException(
                            String.format("Can't use extends link between %s %s!", fromNode.getText(), toNode.getText()),
                            HttpStatus.BAD_REQUEST);
                }
            }
        }
    }

    public void checkIncludeUsage(ClassDiagram classDiagram) {
        for (DLink dLink : classDiagram.getDLinks()) {
            if (dLink.getType().equals(DLinkType.OpenTriangle.name())) {
                DNode fromNode = getNodeForKey(classDiagram.getDNodes(), dLink.getFromNode());
                DNode toNode = getNodeForKey(classDiagram.getDNodes(), dLink.getToNode());
                if (!fromNode.getType().equals(DNodeType.Ellipse.name()) || !toNode.getType().equals(DNodeType.Ellipse.name())) {
                    throw new CustomHttpException(
                            String.format("Can't use includes link between %s %s!", fromNode.getText(), toNode.getText()),
                            HttpStatus.BAD_REQUEST);
                }
            }
        }
    }

    public void checkImplementationUsage(ClassDiagram classDiagram) {
        for (DLink dLink : classDiagram.getDLinks()) {
            if (dLink.getType().equals(DLinkType.OpenTriangle.name())) {
                DNode fromNode = getNodeForKey(classDiagram.getDNodes(), dLink.getFromNode());
                DNode toNode = getNodeForKey(classDiagram.getDNodes(), dLink.getToNode());
                if (!fromNode.getType().equals("class") || !toNode.getType().equals("interface")) {
                    throw new CustomHttpException(
                            String.format("Can't use implementation link between %s %s!", fromNode.getText(), toNode.getText()),
                            HttpStatus.BAD_REQUEST);
                }
            }
        }
    }

    public void checkGeneralisationCount(ClassDiagram classDiagram, Integer count) {
        for (DNode dNode : classDiagram.getDNodes()) {
            List<DLink> linksForNode = classDiagram
                    .getDLinks()
                    .stream()
                    .filter(link -> link.getFromNode().equals(dNode.getNodeKey()))
                    .collect(Collectors.toList());
            int generalisationCount = 0;
            for (DLink dLink : linksForNode) {
                if (dLink.getType().equals(DLinkType.Triangle.name())) {
                    generalisationCount++;
                }
            }
            if (generalisationCount > count) {
                throw new CustomHttpException(
                        String.format("Generalisation count on [%s:%s] is %s, it must be %s!",
                                dNode.getNodeKey(),
                                dNode.getText(),
                                generalisationCount,
                                count),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }

    public void checkFieldTypeValidity(ClassDiagram classDiagram) {
        for (DNode dNode : classDiagram.getDNodes()) {
            for (NodeField nodeField : dNode.getNodeFields()) {
                if (!nodeField.getInfo().equals("string") && !nodeField.getInfo().equals("int")) {
                    if (classDiagram.getDNodes().stream().noneMatch(node -> node.getText().equals(nodeField.getInfo()))) {
                        throw new CustomHttpException(
                                String.format("Unrecognized type %s for field %s in %s!",
                                        nodeField.getInfo(),
                                        nodeField.getText(),
                                        dNode.getText()),
                                HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }
    }

    public void checkClassNameDuplicates(ClassDiagram classDiagram) {
        for (DNode dNode : classDiagram.getDNodes()) {
            for (DNode checkNode : classDiagram.getDNodes()) {
                if (!dNode.getNodeKey().equals(checkNode.getNodeKey()) && dNode.getText().equals(checkNode.getText())) {
                    throw new CustomHttpException(
                            String.format("Duplicated node name %s!", dNode.getText()),
                            HttpStatus.BAD_REQUEST);
                }
            }
        }
    }

    public void checkAuthorToStoryLinks(ClassDiagram classDiagram) {
        for (DNode dNode : classDiagram.getDNodes()) {
            if (dNode.getType().equals(DNodeType.Actor.name())) {
                List<DLink> linksForNode = classDiagram
                        .getDLinks()
                        .stream()
                        .filter(link -> link.getFromNode().equals(dNode.getNodeKey()))
                        .collect(Collectors.toList());
                for (DLink link : linksForNode) {
                    DNode toNode = getNodeForKey(classDiagram.getDNodes(), link.getToNode());
                    if (!link.getType().equals("") && !toNode.getType().equals(DNodeType.Actor.name())) {
                        throw new CustomHttpException(
                                String.format("Can't use %s links from [actor:%s] to [story:%s]!",
                                        link.getType(),
                                        dNode.getText(),
                                        toNode.getText()),
                                HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }
    }

    public void checkGeneralisationUsage(ClassDiagram classDiagram) {
        for (DLink dLink : classDiagram.getDLinks()) {
            if (dLink.getType().equals(DLinkType.Triangle.name())) {
                DNode fromNode = getNodeForKey(classDiagram.getDNodes(), dLink.getFromNode());
                DNode toNode = getNodeForKey(classDiagram.getDNodes(), dLink.getToNode());
                if (classDiagram.getModelType().equals("uml")) {
                    if (fromNode.getType().equals("class")) {
                        if (!toNode.getType().equals("class")) {
                            throw new CustomHttpException(
                                    String.format("Can't use generalisation link between %s %s!", fromNode.getText(), toNode.getText()),
                                    HttpStatus.BAD_REQUEST);
                        }
                    } else if (fromNode.getType().equals("interface")) {
                        if (!toNode.getType().equals("interface")) {
                            throw new CustomHttpException(
                                    String.format("Can't use generalisation link between %s %s!", fromNode.getText(), toNode.getText()),
                                    HttpStatus.BAD_REQUEST);
                        }
                    } else {
                        throw new CustomHttpException(
                                String.format("Can't use generalisation link between %s %s!", fromNode.getText(), toNode.getText()),
                                HttpStatus.BAD_REQUEST);
                    }
                } else {
                    if (!fromNode.getType().equals(DNodeType.Actor.name()) || !toNode.getType().equals(DNodeType.Actor.name())) {
                        throw new CustomHttpException(
                                String.format("Can't use generalisation link between %s %s!", fromNode.getText(), toNode.getText()),
                                HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }
    }

    public DNode getNodeForKey(List<DNode> dNodes, Integer key) {
        for (DNode dNode : dNodes) {
            if (dNode.getNodeKey().equals(key)) {
                return dNode;
            }
        }
        return null;
    }

    public MetaData getMetaDataByModelType(String modelType) {
        return metaDataRepository.findByModelType(modelType)
                .orElseThrow(() -> new CustomHttpException(
                        String.format("Unrecognized model type %s!", modelType),
                        HttpStatus.BAD_REQUEST));
    }
}
